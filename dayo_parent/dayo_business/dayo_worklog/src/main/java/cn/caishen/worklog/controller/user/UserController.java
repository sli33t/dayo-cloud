package cn.caishen.worklog.controller.user;

import cn.caishen.common.domain.po.User;
import cn.caishen.common.utils.JSONUtil;
import cn.caishen.common.utils.LbMap;
import cn.caishen.common.utils.MD5Util;
import cn.caishen.serviceinterface.system.UserService;
import cn.caishen.worklog.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    //private final static Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

    @DubboReference
    private UserService dayoUserService;

    /**
     * 跳转列表页面
     * @return
     */
    @GetMapping(value = "/toUserList")
    public ModelAndView toUserList(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/user-list");
        return mv;
    }


    /**
     * 查询所有用户信息
     * @return
     */
    @GetMapping(value = "/findAll")
    public LbMap findAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "") String jsonStr){
        try {
            LbMap param = LbMap.fromObject(jsonStr);
            PageInfo<User> pages = dayoUserService.findAll(page, limit, param);
            logger.info("用户查询成功");
            return LbMap.successResult("用户查询成功", pages.getList(), pages.getTotal());
        }catch (Exception e){
            return LbMap.failResult("用户查询失败，"+e.getMessage());
        }
    }

    /**
     * 查询岗位用户信息
     * @param page
     * @param limit
     * @param roleId
     * @return
     */
    @GetMapping(value = "/findByRoleId")
    public LbMap findByRoleId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit, String roleId){
        try {
            if (roleId.equals("")){
                return LbMap.failResult("请选择岗位");
            }
            PageInfo<LbMap> pages = dayoUserService.findByRoleId(page, limit, roleId);
            logger.info("用户查询成功");
            return LbMap.successResult("用户查询成功", pages.getList(), pages.getTotal());
        }catch (Exception e){
            return LbMap.failResult("用户查询失败，"+e.getMessage());
        }
    }


    /**
     * 跳转新增页面
     * @return
     */
    @GetMapping(value = "/toAdd")
    public ModelAndView toAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("user/user-add");
        return mv;
    }

    /**
     * 通过编号查询用户信息
     * @param userId
     * @return
     */
    @GetMapping(value = "/toUpdate")
    public ModelAndView toUpdate(String userId){
        ModelAndView mv = new ModelAndView();
        try {
            if (userId.equals("")){
                throw new Exception("没有找到用户编号");
            }

            User user = dayoUserService.findById(userId);
            if (user!=null){
                mv.addObject("user", user);
                mv.setViewName("user/user-update");
            }else {
                mv.setViewName("system/error");
                mv.addObject("errorMsg", "没有找到用户信息");
            }
            //logger.info("查询成功");
            return mv;
        }catch (Exception e){
            mv.setViewName("system/error");
            mv.addObject("errorMsg", e.getMessage());
            logger.info("查询成功");
            return mv;
        }
    }

    /**
     * 新增和修改
     * @param user
     * @return
     */
    @PostMapping(value = "/edit")
    public LbMap edit(User user) {
        try {
            if (user.getUsername().equals("")){
                return LbMap.failResult("用户编辑失败，用户名称不能为空！");
            }else if (user.getTelNo().equals("")){
                return LbMap.failResult("用户编辑失败，用户电话不能为空！");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            user.setCreateTime(dateFormat.parse(dateFormat.format(new Date())));
            user.setDeleteFlag(0);

            //新增
            if (StringUtils.isEmpty(user.getUserId())){
                List<User> userList = dayoUserService.checkUserInfo(user.getTelNo(), user.getUsername());

                if (userList!=null&&userList.size()>0){
                    for (User oldUser : userList) {
                        if (oldUser.getUsername().equals(user.getUsername())){
                            return LbMap.failResult("用户名称已经存在");
                        }else if (oldUser.getTelNo().equals(user.getTelNo())){
                            return LbMap.failResult("用户电话已经存在");
                        }
                    }
                }

                if (!user.getPassword().equals("")){
                    user.setPassword(MD5Util.md5(user.getPassword(), user.getTelNo()));
                }

                //初始化的行版本号为0
                user.setRowVersion(0);
                //ID为空的为新增
                dayoUserService.save(user);
            }else {
                //ID不为空的为修改
                dayoUserService.update(user);
            }

            return LbMap.successResult("用户编辑成功");
        }catch (Exception e){
            logger.info("用户编辑失败："+e.getMessage());
            return LbMap.failResult("用户编辑失败，"+e.getMessage());
        }
    }

    /**
     * 删除
     * @param userId
     * @return
     */
    @PostMapping(value = "/delete")
    public LbMap delete(String userId, String rowVersion){
        try {
            if (userId.equals("")){
                return LbMap.failResult("用户删除失败，没有找到用户编号！");
            }
            dayoUserService.delete(userId, rowVersion);
            logger.info(userId+"用户删除成功");
            return LbMap.successResult("用户删除成功");
        }catch (Exception e){
            return LbMap.failResult("用户删除失败，"+e.getMessage());
        }
    }

}