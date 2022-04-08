package com.hower.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hower.hotel.mapper.WorkMapper;
import com.hower.hotel.model.entity.Work;
import com.hower.hotel.service.IWorkService;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {
}
