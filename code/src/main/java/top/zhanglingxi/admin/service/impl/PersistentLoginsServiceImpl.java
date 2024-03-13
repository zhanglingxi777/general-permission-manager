package top.zhanglingxi.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zhanglingxi.admin.domain.PersistentLogins;
import top.zhanglingxi.admin.mapper.PersistentLoginsMapper;
import top.zhanglingxi.admin.service.IPersistentLoginsService;

@Transactional
@Service
public class PersistentLoginsServiceImpl extends ServiceImpl<PersistentLoginsMapper, PersistentLogins> implements IPersistentLoginsService {
}
