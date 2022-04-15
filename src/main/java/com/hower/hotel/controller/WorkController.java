package com.hower.hotel.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.google.common.collect.Lists;
import com.hower.hotel.common.responses.ApiResponses;
import com.hower.hotel.framework.controller.SuperController;
import com.hower.hotel.model.dto.WorkDTO;
import com.hower.hotel.model.dto.WorkDTO1;
import com.hower.hotel.model.entity.*;
import com.hower.hotel.service.impl.StaffInfoServiceImpl;
import com.hower.hotel.service.impl.StaffRoleServiceImpl;
import com.hower.hotel.service.impl.SysTokenServiceImpl;
import com.hower.hotel.service.impl.WorkServiceImpl;
import com.hower.hotel.utils.EasyPoiUtils;
import com.hower.hotel.utils.ExcelUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

//    @GetMapping("/page")
//    @ApiOperation("/工作进度页 分页查询")
//    public ApiResponses<IPage<Work>> getCustomerInfoList(
//            @RequestParam(defaultValue = "1", required = false) Integer current,
//            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
//            @RequestParam("type") String type,
//            @RequestParam("address") String address,
//            @RequestParam("phone") String phone,
//            @RequestParam("status") Integer status,
//            @RequestParam("isAdmin") Integer isAdmin, HttpServletRequest request) {
//        QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
//        if (isAdmin.intValue() != 0) {
//            String token = null;
//            Cookie[] cookie = request.getCookies();
//            for (int i = 0; i < cookie.length; i++) {
//                Cookie cook = cookie[i];
//                if (cook.getName().equalsIgnoreCase("token")) { //获取键
//                    token = cook.getValue().toString();
//                }
//            }
//            SysToken sysToken = sysTokenService.findByToken(token);
//            if (ObjectUtil.isEmpty(sysToken)) {
//                return failure(new Page<Work>());
//            }
//            workQueryChainWrapper.eq(Work.ALLOT_ID, sysToken.getSId());
//        }
//        if (!(type).equals("null")) {
//            workQueryChainWrapper.eq(Work.TYPE, type);
//        }
//        if (!address.equals("null")) {
//            workQueryChainWrapper.like(Work.AADDRESS, address);
//        }
//        if (!phone.equals("null")) {
//            workQueryChainWrapper.like(Work.PHONE, phone);
//        }
//        if (status != null) {
//            workQueryChainWrapper.eq(Work.STATUS, status);
//        }
//        IPage<Work> list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
//        list.getRecords().forEach(bean -> {
//            StaffInfo serviceById = staffInfoService.getById(bean.getUserId());
//            bean.setUserName(serviceById.getName());
//        });
//        return success(list);
//    }


    @GetMapping("/pageByType")
    @ApiOperation("/工作进度页 分页查询")
    public ApiResponses<IPage<Work>> getCustomerInfoList(
            @RequestParam(defaultValue = "1", required = false) Integer current,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam("type") Integer type,
            @RequestParam("worktype") String worktype,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("status") String status,
            @RequestParam("isAdmin") Integer isAdmin, HttpServletRequest request) {
        QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
        if (isAdmin.intValue() != 0) {
            String token = null;
            Cookie[] cookie = request.getCookies();
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue().toString();
                }
            }
            SysToken sysToken = sysTokenService.findByToken(token);
            if (ObjectUtil.isEmpty(sysToken)) {
                return failure(new Page<Work>());
            }
            workQueryChainWrapper.eq(Work.ALLOT_ID, sysToken.getSId());
        }
        if (!(worktype).equals("null")) {
            workQueryChainWrapper.eq(Work.TYPE, worktype);
        }
        if (!address.equals("null")) {
            workQueryChainWrapper.like(Work.AADDRESS, address);
        }
        if (!phone.equals("null")) {
            workQueryChainWrapper.like(Work.PHONE, phone);
        }
        if (!status.equals("null")) {
            workQueryChainWrapper.eq(Work.STATUS, status);
        }
        IPage<Work> list = null;
        if (type == 1) {
            workQueryChainWrapper.isNotNull(Work.ALLOT_ID);
            workQueryChainWrapper.in(Work.STATUS,1,2);
            list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
        } else if (type == 2) {
            workQueryChainWrapper.notIn(Work.STATUS, 1,3, 4);
            workQueryChainWrapper.isNull(Work.ALLOT_FINISH_TIME);
            workQueryChainWrapper.apply("now()-finish_time<=1000000");
            list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
        } else if (type == 3) {
            workQueryChainWrapper.in(Work.STATUS,2);
            workQueryChainWrapper.apply("now() > finish_time ");
            list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
        } else if (type == 4) {
            workQueryChainWrapper.eq(Work.STATUS, 4);
            list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
        } else if (type == 5) {
            workQueryChainWrapper.isNull(Work.ALLOT_ID);
            list = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
        }
        list.getRecords().forEach(bean -> {
            StaffInfo serviceById = staffInfoService.getById(bean.getUserId());
            bean.setUserName(serviceById.getName());
        });
        return success(list);
    }

    @GetMapping("/yinbs/pageByType")
    @ApiOperation("/工作进度页应包商 分页查询")
  public   ApiResponses<IPage<Work>> aa(@RequestParam(defaultValue = "1", required = false) Integer current,
                                        @RequestParam(defaultValue = "10", required = false) Integer pageSize, @RequestParam("type") Integer type,
             @RequestParam("worktype") String worktype,
             @RequestParam("address") String address,
             @RequestParam("phone") String phone, HttpServletRequest request){
        QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
            String token = null;
            Cookie[] cookie = request.getCookies();
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue().toString();
                }
            }
      IPage<Work> list = null;
            SysToken sysToken = sysTokenService.findByToken(token);
            if (ObjectUtil.isEmpty(sysToken)) {
                return failure(new Page<Work>());
            }
            workQueryChainWrapper.eq(Work.ALLOT_ID, sysToken.getSId());
        if (!(worktype).equals("null")) {
            workQueryChainWrapper.eq(Work.TYPE, worktype);
        }
        if (!address.equals("null")) {
            workQueryChainWrapper.like(Work.AADDRESS, address);
        }
        if (!phone.equals("null")) {
            workQueryChainWrapper.like(Work.PHONE, phone);
        }
            if(type==1){
                workQueryChainWrapper.in(Work.STATUS,1,4);
            }else if(type==2){
                workQueryChainWrapper.eq(Work.STATUS,2);
            }
      list= workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).page(new Page<Work>(current, pageSize, true));
      list.getRecords().forEach(bean -> {
          StaffInfo serviceById = staffInfoService.getById(bean.getUserId());
          bean.setUserName(serviceById.getName());
      });
      return success(list);
    }

    @GetMapping("/typeCount")
    public ApiResponses<Map<Integer,Integer>> getCustomerInfoList1() {
        Map<Integer,Integer>map=new HashMap<>();
            Integer [] arr={1,2,3,4,5,6};
            Arrays.stream(arr).forEach(bean->{
                Integer count=0;
                QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
                if (bean == 1) {
                    workQueryChainWrapper.isNotNull(Work.ALLOT_ID);
                    workQueryChainWrapper.in(Work.STATUS,1,2);
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                } else if (bean == 2) {
                    workQueryChainWrapper.notIn(Work.STATUS, 1,3, 4);
                    workQueryChainWrapper.isNull(Work.ALLOT_FINISH_TIME);
                    workQueryChainWrapper.apply("now()-finish_time<=1000000");
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                } else if (bean == 3) {
                    workQueryChainWrapper.in(Work.STATUS,2);
                    workQueryChainWrapper.apply("now() > finish_time ");
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                } else if (bean == 4) {
                    workQueryChainWrapper.eq(Work.STATUS, 4);
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                } else if (bean == 5) {
                    workQueryChainWrapper.isNull(Work.ALLOT_ID);
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                }
                map.put(bean,count);
            });
        return success(map);
    }

    @GetMapping("/count")
    public ApiResponses<Map<Integer,Integer>> getCustomerInfoList2(HttpServletRequest request) {
        Map<Integer,Integer>map=new HashMap<>();
            String token = null;
            Cookie[] cookie = request.getCookies();
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue().toString();
                }
            }
            SysToken sysToken = sysTokenService.findByToken(token);
            if (ObjectUtil.isEmpty(sysToken)) {
                map.put(1,0);
                map.put(2,0);
                return success(map);
            }
            Integer [] arr={1,2};
            Arrays.stream(arr).forEach(bean->{
                Integer count=0;
                QueryChainWrapper<Work> workQueryChainWrapper = workService.query();
                workQueryChainWrapper.eq(Work.ALLOT_ID, sysToken.getSId());
                if (bean == 1) {
                    workQueryChainWrapper.in(Work.STATUS,1,4);
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                } else if (bean == 2) {
                    workQueryChainWrapper.notIn(Work.STATUS, 2);
                    count = workQueryChainWrapper.orderByDesc(Work.CREATE_TIME).list().size();
                }
                map.put(bean,count);
            });
        return success(map);
    }

    @GetMapping("/getUser")
    @ApiOperation("/分包商下拉")
    public ApiResponses<List<StaffInfo>> getUser() {
        List<StaffInfo> list = Lists.newArrayList();
        Map<Integer, StaffInfo> staffInfoMap = staffInfoService.query().eq(StaffInfo.STATUS, 1).list().stream().collect(Collectors.toMap(i -> i.getId(), y -> y));
        staffRoleService.list().forEach(bean -> {
            StaffInfo staffInfo = staffInfoMap.get(bean.getUid());
            if (BeanUtil.isNotEmpty(staffInfo)) {
                if (!bean.getRoleId().equals(1)) {
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
    @ApiOperation("添加 或者 修改")
    public ApiResponses<Boolean> postCustomerInfoByEntity(@RequestBody WorkDTO1 work1, HttpServletRequest request) throws GeneralSecurityException, MessagingException {
        if (work1.getId() == null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Work work = new Work();
            work.setAddress(work1.getAddress());
            work.setCode(work1.getCode());
            work.setContent(work1.getContent());
            LocalDateTime ldt = LocalDateTime.parse(work1.getFinishTime(),df);
            work.setFinishTime(ldt);
            work.setPhone(work1.getPhone());
            work.setPrice(work1.getPrice());
            work.setType(work1.getType());
            String token = null;
            Cookie[] cookie = request.getCookies();
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue().toString();
                }
            }
            if(token!=null){
                SysToken sysToken = sysTokenService.findByToken(token);
                StaffInfo staffInfo = staffInfoService.getById(sysToken.getSId());
                work.setUserId(staffInfo.getId());
                work.setUserName(staffInfo.getName());
            }
            work.setCreateTime(LocalDateTime.now());
            work.setPaymentStatus(2);
            work.setStatus(3);
            return success(workService.saveOrUpdate(work));
        }
        if (work1.getStatus() == 1) {//分包商确认发邮箱
            Work work = new Work();
            Work workServiceById = workService.getById(work1.getId());
            StaffInfo userStaffInfo = staffInfoService.getById(workServiceById.getUserId());
            StaffInfo allotStaffInfo = staffInfoService.getById(workServiceById.getAllotId());
            sendEmail(allotStaffInfo.getEmail(), allotStaffInfo.getEmailPassword(), userStaffInfo.getEmail(), workServiceById.getContent(), 1);
            work.setAllotFinishTime(LocalDateTime.now());
            return success(workService.saveOrUpdate(work));
        }
        Work work = BeanUtil.copyProperties(work1, Work.class);
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
    @ApiOperation("/选择了分包商后确认 分包商id 工作id 分包商价格")
    public ApiResponses<Boolean> sureByEntity(@RequestBody Work work, HttpServletRequest request) {
        if (BeanUtil.isNotEmpty(work.getId())) {
            String token = null;
            Cookie[] cookie = request.getCookies();
            for (int i = 0; i < cookie.length; i++) {
                Cookie cook = cookie[i];
                if (cook.getName().equalsIgnoreCase("token")) { //获取键
                    token = cook.getValue().toString();
                }
            }
            SysToken sysToken = sysTokenService.findByToken(token);
            StaffInfo userStaffInfo = staffInfoService.getById(sysToken.getSId());
            StaffInfo allotStaffInfo = staffInfoService.getById(work.getAllotId());
            try {
                Work serviceById = workService.getById(work.getId());
                //发邮件通知商家
                sendEmail(userStaffInfo.getEmail(), userStaffInfo.getEmailPassword(), allotStaffInfo.getEmail(), serviceById.getContent(), 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        work.setPaymentStatus(2);
        work.setStatus(2);
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
    @ApiOperation("admin上传csv 修改工作代码对应的 分包商支付价格 与状态 利润")
    @PostMapping("/importCsvFile")
    public ApiResponses<Boolean> importCsvFile(@RequestParam MultipartFile file) throws IOException {
        byte[] bate = file.getBytes();

        List<Map<String, Object>> list = getResource(bate);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    continue;
                }
                //修改利润  支付状态
                int finalI = i;
                List<Work> workList = workService.list().stream().filter(y -> y.getCode().equalsIgnoreCase(list.get(finalI).get("content").toString())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(workList)) {
                    Work work = workList.get(0);
                    if (new BigDecimal(list.get(i).get("profits").toString()).compareTo(work.getAllotPrice()) >= 0) {
                        work.setProfits(new BigDecimal(list.get(i).get("profits").toString()).subtract(work.getPrice()));
                        work.setPaymentStatus(1);
                        workService.saveOrUpdate(work);
                    }
                }
            }
            return success(true);
        }
        return success(false);
    }

    @ApiOperation("admin上传execl 新增工作")
    @RequestMapping(value = "/importData", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponses<Boolean> importData(HttpServletRequest request, @RequestPart(value = "file") MultipartFile file) {
        return success(importExcel(file));
    }

    public Boolean importExcel(MultipartFile file) {

        StaffInfo serviceById = staffInfoService.getById(10000);
        try {
            InputStream inputStream = file.getInputStream();
            List list = ExcelUtil.readExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            for (int i = 0; i < list.size(); i++) {
                Work work = new Work();
                work.setCode(((ArrayList) list.get(i)).get(0).toString());
                work.setContent(((ArrayList) list.get(i)).get(1).toString());
                work.setType(((ArrayList) list.get(i)).get(2).toString());
                work.setAddress(((ArrayList) list.get(i)).get(3).toString());
                LocalDateTime finishTIme = LocalDateTime.parse(((ArrayList) list.get(i)).get(4).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                work.setFinishTime(finishTIme);
                work.setPrice(new BigDecimal(((ArrayList) list.get(i)).get(5).toString()));
                work.setCreateTime(LocalDateTime.now());
                work.setStatus(3);
                work.setPhone(serviceById.getContact());
                work.setUserId(serviceById.getId());
                work.setUserName(serviceById.getName());
                workService.save(work);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @ApiOperation("只有 admin 导出利润")
    @GetMapping("/exportProfitsExcel")
    public void export(HttpServletResponse response) throws UnsupportedEncodingException {
        //得到所有要导出的数据
        try {
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=user.xls");
            // =========easypoi部分
            List<Work> personList = workService.list(new QueryWrapper<Work>().eq(Work.PAYMENT_STATUS, 1));
            if (personList.size() == 0) {
                return;
            }
            ExportParams deptExportParams = new ExportParams();
            // 设置sheet得名称
            deptExportParams.setSheetName("利润报表");
            deptExportParams.setTitle("利润报表");

            Map<String, Object> deptExportMap = new HashMap<>();
            // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
            deptExportMap.put("title", deptExportParams);
            // 模版导出对应得实体类型
            deptExportMap.put("entity", Work.class);
            // sheet中要填充得数据
            deptExportMap.put("data", personList);

            List<Map<String, Object>> sheetsList = new ArrayList<>();
            sheetsList.add(deptExportMap);
            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("完成项目报表")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, HttpServletRequest request) {
        // 模拟从数据库获取需要导出的数据
        String token = null;
        Cookie[] cookie = request.getCookies();
        for (int i = 0; i < cookie.length; i++) {
            Cookie cook = cookie[i];
            if (cook.getName().equalsIgnoreCase("token")) { //获取键
                token = cook.getValue().toString();
            }
        }
        SysToken sysToken = sysTokenService.findByToken(token);
        StaffInfo userStaffInfo = staffInfoService.getById(sysToken.getSId());
        StaffRole roleServiceOne = staffRoleService.getOne(new QueryWrapper<StaffRole>().eq("uid", userStaffInfo.getId()));
        List<Work> personList = Lists.newArrayList();
        if (roleServiceOne.getRoleId() == 1) {
            personList = workService.list(new QueryWrapper<Work>().eq(Work.STATUS, 4));
        } else {
            personList = workService.list(new QueryWrapper<Work>().eq(Work.STATUS, 4).eq(Work.ALLOT_ID, userStaffInfo.getId()));
        }
        List<WorkDTO> dtoList = new ArrayList<>();
        personList.forEach(bean -> {
            WorkDTO workDTO = new WorkDTO();
            BeanUtil.copyProperties(bean, workDTO);
            dtoList.add(workDTO);
        });
        // 导出操作
        EasyPoiUtils.exportExcel(dtoList, "完成项目报表", "完成项目报表", WorkDTO.class, "完成项目报表.xls", response);
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
    static void sendEmail(String email, String password, String fromEmail, String content, Integer type) throws GeneralSecurityException, MessagingException {
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

        if (type == 1) {
            mimeMessage.setSubject("分包商完成确认");
        } else {
            mimeMessage.setSubject("Admin下发工作");
        }

        mimeMessage.setContent(content, "text/html;charset=UTF-8");

        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        transport.close();
    }
}
