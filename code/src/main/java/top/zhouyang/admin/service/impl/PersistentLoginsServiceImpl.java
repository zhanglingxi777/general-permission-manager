package top.zhouyang.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhouyang.admin.domain.PersistentLogins;
import top.zhouyang.admin.mapper.PersistentLoginsMapper;
import top.zhouyang.admin.service.IPersistentLoginsService;

@Transactional
@Service
public class PersistentLoginsServiceImpl extends ServiceImpl<PersistentLoginsMapper, PersistentLogins> implements IPersistentLoginsService {
}
