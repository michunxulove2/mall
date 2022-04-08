package com.hower.hotel.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.google.common.collect.Lists;
import com.hower.hotel.common.responses.ApiResponses;
import com.hower.hotel.framework.controller.SuperController;
import com.hower.hotel.model.entity.*;
import com.hower.hotel.service.IWorkService;
import com.hower.hotel.service.impl.StaffInfoServiceImpl;
import com.hower.hotel.service.impl.StaffRoleServiceImpl;
import com.hower.hotel.service.impl.SysTokenServiceImpl;
import com.hower.hotel.service.impl.WorkServiceImpl;
import com.hower.hotel.utils.CsvImportUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hower.hotel.utils.CsvImportUtil.getResource;

@Api(tags = {"工作信息"})
@RequestMapping(value = "/work")
@RestController
public class WorkController extends SuperController {

    @Autowired
    private WorkServiceImpl workService;

    @Autowired
    private SysTokenServiceImpl sysTokenService;

    @Autowired
    private StaffInfoServiceImpl staffInfoService;

    @Autowired
    private StaffRoleServiceImpl staffRoleService;

    @GetMapping("/page")
    @ApiOperation("/page")
    public ApiResponses<IPage<Work>> getCustomerInfoList(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam("type") Integer type,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("status") Integer status,
            @RequestParam("token") String token) {
        SysToken sysToken = sysTokenService.findByToken(token);
        if (ObjectUtil.isEmpty(sysToken)) {
            return failure(new Page<Work>());
        }
        System.out.println(current);
        QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
        workQueryChainWrapper.eq(Work.ALLOTID, sysToken.getId());
        if (type != null) {
            workQueryChainWrapper.eq(Work.TYPE, type);
        }
        if (address != null) {
            workQueryChainWrapper.like(Work.AADDRESS, address);
        }
        if (phone != null) {
            workQueryChainWrapper.like(Work.PHONE, phone);
        }
        if (status != null) {
            workQueryChainWrapper.eq(Work.STATUS, status);
        }
        IPage<Work> list = workQueryChainWrapper.orderByDesc(Work.CREATETIME).page(new Page<Work>(current, pageSize, true));
        return success(list);
    }

    @GetMapping("/getUser")
    @ApiOperation("/分包商下拉")
    public ApiResponses<List<StaffInfo>> getUser() {

        List<StaffInfo> list = Lists.newArrayList();

        QueryChainWrapper<StaffInfo> staffInfoQueryChainWrapper = staffInfoService.query();
        staffInfoQueryChainWrapper.eq(StaffInfo.STATUS, 1);
        staffInfoQueryChainWrapper.isNull(StaffInfo.EMAIL_PASSWORD);
        Map<Integer, StaffInfo> staffInfoMap = staffInfoService.list(staffInfoQueryChainWrapper).stream().collect(Collectors.toMap(i -> i.getId(), y -> y));

        staffRoleService.list().forEach(bean -> {
            StaffInfo staffInfo = staffInfoMap.get(bean.getUid());
            if (BeanUtil.isNotEmpty(staffInfo)) {
                if (bean.getRoleId().equals(1)) {
                    list.add(staffInfo);
                }
            }
        });
        return success(list);
    }

    /**
     * 修改 添加
     *
     * @param work
     * @return
     */
    @CrossOrigin
    @PostMapping("/post")
    @ApiOperation("/post")
    @RequiresRoles("admin")
    public ApiResponses<Boolean> postCustomerInfoByEntity(@RequestBody Work work) {
        return success(workService.saveOrUpdate(work));
    }

    /**
     * 选择了分包商后确认
     *
     * @param work
     * @return
     */
    @CrossOrigin
    @PostMapping("/sure")
    @ApiOperation("/sure")
    @RequiresRoles("admin")
    public ApiResponses<Boolean> sureByEntity(@RequestBody Work work) {
        if (BeanUtil.isNotEmpty(work.getId())) {
            StaffInfo userStaffInfo = staffInfoService.getById(work.getUserId());
            StaffInfo allotStaffInfo = staffInfoService.getById(work.getAllotId());
            try {
                //发邮件通知商家
                sendEmail(userStaffInfo.getEmail(), userStaffInfo.getPassword(), allotStaffInfo.getEmail(), work.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success(workService.saveOrUpdate(work));
    }

    @ApiOperation("/{id}")
    @DeleteMapping("/{id}")
    public ApiResponses<Boolean> delById(@PathVariable Integer id) {
        return success(workService.removeById(id));
    }

    /**
     * 上传CSV文件
     *
     * @param file
     * @throws IOException
     */
    @RequestMapping("/importCsvFile")
    public void importCsvFile(@RequestParam MultipartFile file) throws IOException {
        byte[] bate = file.getBytes();
        List<Map<String, Object>> list = getResource(bate);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    continue;
                }
                //修改利润  支付状态
                List<Work> workList = workService.list().stream().filter(y -> y.getContent().equalsIgnoreCase(list.get(i).get("content"))).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(workList)) {
                    Work work = workList.get(0);
                    if (new BigDecimal(list.get(i).get("profits").toString()).compareTo(work.getAllotPrice()) >= 0) {
                        work.setProfits(new BigDecimal(list.get(i).get("profits").toString()).subtract(work.getPrice()));
                        work.setPaymentStatus(1);
                        workService.saveOrUpdate(work);
                    }
                }
            }
        }
    }

    /**
     * 发邮件
     *
     * @param email
     * @param password
     * @param fromEmail
     * @param content
     * @throws GeneralSecurityException
     * @throws MessagingException
     */
    static void sendEmail(String email, String password, String fromEmail, String content) throws GeneralSecurityException, MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        session.setDebug(true);

        Transport transport = session.getTransport();

        transport.connect("smtp.qq.com", email, password);

        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setFrom(new InternetAddress(email));

        mimeMessage.setRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(fromEmail)));

        mimeMessage.setSubject("分包商工作");

        mimeMessage.setContent(content, "text/html;charset=UTF-8");

        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        transport.close();
    }
}
