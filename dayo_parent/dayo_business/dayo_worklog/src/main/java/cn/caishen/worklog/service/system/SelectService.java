package cn.caishen.worklog.service.system;

import cn.caishen.common.domain.po.Version;
import cn.caishen.common.utils.LbMap;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author LB
 */
public interface SelectService {

    PageInfo<LbMap> findArea(int page, int limit);

    PageInfo<LbMap> findCustomer(int page, int limit);

    List<Version> findVersion();

    PageInfo<LbMap> findUser(int page, int limit);
}
