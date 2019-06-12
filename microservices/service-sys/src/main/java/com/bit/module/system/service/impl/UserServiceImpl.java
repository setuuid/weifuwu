package com.bit.module.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.*;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.RefreshTokenVO;
import com.bit.module.system.vo.UserVO;
import com.bit.module.system.vo.VaildCodeVO;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    public UserDao userDao;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private UserRelIdentityDao userRelIdentityDao;

    @Autowired
    private IdentityDao identityDao;

    @Autowired
    private UserRelPbOrgDao userRelPbOrgDao;

    @Autowired
    private PbOrganizationDao pbOrganizationDao;

    @Autowired
    private UserRelAppDao userRelAppDao;

    @Autowired
    private DictDao dictDao;

    @Autowired
    private OaDepartmentDao oaDepartmentDao;

    @Autowired
    private UserRelOaDepDao userRelOaDepDao;

    @Value("${atToken.expire}")
    private String atTokenExpire;
    @Value("${rtToken.expire}")
    private String rtTokenExpire;
    /**
     * 登陆
     * @param userVO
     * @return
     */
    @Override
    public BaseVo login(UserVO userVO)  {
        User user = userDao.findByUsername(userVO.getUsername());
        String pw =  MD5Util.compute(userVO.getPassword() + user.getSalt());
        if (!pw.equals(user.getPassword())) {
            throw new BusinessException("密码不正确");
        }
        if (userVO.getTerminalId()==null) {
            throw new BusinessException("接入端id不能为空");
        }
        int count=identityDao.findCountbyAppId(userVO.getAppId());
        if (count==0){
            throw new BusinessException("此用户在此应用下没有身份");
        }
        UserInfo userInfo = new UserInfo();
        userVO.setId(user.getId());
        userInfo.setId(user.getId());
        List<UserRelPbOrg> pbOrgs=userRelPbOrgDao.findByUserId(user.getId());
        if (pbOrgs.size()>0){
            userInfo.setPbOrgId(pbOrgs.get(0).getPborgId());
        }
        userInfo.setUsername(user.getUsername());
        String json = JSON.toJSONString(userInfo);
        String token = UUIDUtil.getUUID();
        //at token 失效时间为1天
        cacheUtil.set(Const.TOKEN_PREFIX+userVO.getTerminalId()+":"+token, json,Long.valueOf(atTokenExpire));
        //rt token 失效时间为7天
        String rtToken = UUIDUtil.getUUID();
        RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
        refreshTokenVO.setUserInfo(userInfo);
        refreshTokenVO.setAtKey(Const.TOKEN_PREFIX+userVO.getTerminalId()+":"+token);
        String rtJson = JSON.toJSONString(refreshTokenVO);
        cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + userVO.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));
        Map map = new HashMap<>();
        map.put("token", token);
        map.put("refreshToken", rtToken);
        if (pbOrgs.size()>0){
            PbOrganization byId = pbOrganizationDao.findById(pbOrgs.get(0).getPborgId());
            map.put("pbOrganization",byId);
        }
        List<Identity> identitys = identityDao.findByUserId(user.getId());
        map.put("identitys",identitys);
        userVO.setData(map);
        return userVO;
    }


    @Override
    public BaseVo list(UserVO userVO) {
        UserInfo userInfo = getCurrentUserInfo();
        userVO.setData(userDao.findByConditionPage(userVO));
        return userVO;
    }

    /**
     * 分页模糊查询
     * @param userVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(UserVO userVO) {
        PageHelper.startPage(userVO.getPageNum(), userVO.getPageSize());
        List<User> list = userDao.findByConditionPage(userVO);
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查看
     * @param id
     * @return
     */
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    /**
     * 查看---业务
     * @param id
     * @return
     */
    @Override
    public User findRealById(Long id) {
        User user = userDao.findById(id);
        List<Identity> identities=identityDao.findByUserId(id);
        List<PbOrganization> pbOrganizations=pbOrganizationDao.findByUserId(id);
        if (pbOrganizations.size()>0){
            for (PbOrganization pbOrganization : pbOrganizations) {
                pbOrganization.setStrId(RadixUtil.toFullBinaryString(pbOrganization.getId()));
            }
        }
        List<OaDepartment> oaDepartments=oaDepartmentDao.findByUserId(id);
        user.setOaDepartments(oaDepartments);

        List<UserRelApp> userRelApps=userRelAppDao.findByUserId(id);
        List<Integer> integers = new ArrayList<>();
        for (UserRelApp userRelApp : userRelApps) {
            integers.add(userRelApp.getAppId());
        }
        user.setIdentities(identities);
        user.setPbOrganizations(pbOrganizations);
        user.setApps(integers);
        return user;
    }

    /**
     * 校验username唯一
     * @param username
     * @return
     */
    @Override
    public int checkUsername(String username) {
        return userDao.findCountByUsername(username);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public BaseVo registerUser(User user) {
        //TODO 注册
        return null;
    }

    /**
     * 新增
     * @param user
     */
    @Override
    @Transactional
    public void add(User user)  {
        //随机密码盐
        String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);
        //密码和盐=新密码  md5加密新密码
        String password= MD5Util.compute(Const.RESET_PASSWORD + salt);
        user.setPassword(password);
        user.setSalt(salt);
        user.setCreateType(Const.USER_CREATE_TYPE);
        user.setStatus(Const.USER_STATUS);
        user.setInsertTime(new Date(new Date().getTime()));
        userDao.add(user);
        Long userId = user.getId();
        this.batchAddIdent(userId,user);
        this.batchAddPbOrg(userId,user);
        this.batchAddDict(userId,user);
        this.batchAddOaOrg(userId,user);
    }

    /**
     * 保存政务
     * @param userId
     * @param user
     */
    @Transactional
     void batchAddOaOrg(Long userId, User user) {
        List<OaDepartment> oaDepartments = user.getOaDepartments();
        if (oaDepartments!=null){
            UserRelOaDep userRelOaDep = new UserRelOaDep();
            List<UserRelOaDep> userRelOaDeps = new ArrayList<>();
            for (OaDepartment oaDepartment : oaDepartments) {
                userRelOaDep.setUserId(userId);
                userRelOaDep.setDepId(oaDepartment.getId());
                userRelOaDeps.add(userRelOaDep);
            }
            userRelOaDepDao.batchAdd(userRelOaDeps);
        }
    }

    /**
     * 保存字典 app
     * @param userId
     * @param user
     */
     @Transactional
     void batchAddDict(Long userId, User user) {
         List<Integer> apps = user.getApps();
         UserRelApp userRelApp = new UserRelApp();
         List<UserRelApp> userRelApps = new ArrayList<>();
         for (Integer app : apps) {
             userRelApp.setUserId(userId);
             userRelApp.setAppId(app);
             userRelApps.add(userRelApp);
         }
        userRelAppDao.batchAdd(userRelApps);
    }

    /**
     * 保存用户和党建组织中间表
     * @param userId
     * @param user
     */
     @Transactional
     void batchAddPbOrg(Long userId, User user) {
         List<PbOrganization> pbOrganizations = user.getPbOrganizations();
         if(pbOrganizations!=null){
             UserRelPbOrg userRelPbOrg = new UserRelPbOrg();
             List<UserRelPbOrg> userRelPbOrgs = new ArrayList<>();
             for (PbOrganization pbOrganization : pbOrganizations) {
                 Long pborgId = RadixUtil.toLong(pbOrganization.getStrId());
                 userRelPbOrg.setUserId(userId);
                 userRelPbOrg.setPborgId(pborgId);
                 userRelPbOrgs.add(userRelPbOrg);
             }
             userRelPbOrgDao.batchAdd(userRelPbOrgs);
         }
    }

    /**
     * 保存用户和身份中间表
     * @param userId
     * @param user
     */
    @Transactional
    void batchAddIdent(Long userId,User user) {
        List<Identity> identities = user.getIdentities();
        UserRelIdentity userRelIdentity = new UserRelIdentity();
        List<UserRelIdentity> userRelIdentities = new ArrayList<>();
        for (Identity identity : identities) {
            userRelIdentity.setIdentityId(identity.getId());
            userRelIdentity.setUserId(userId);
            userRelIdentities.add(userRelIdentity);
        }
        userRelIdentityDao.batchAdd(userRelIdentities);
    }

    /**
     * 修改
     * @param user
     */
    @Override
    @Transactional
    public void update(User user) {
        Long userId = user.getId();
        userRelIdentityDao.delByUserId(userId);
        userRelPbOrgDao.delByUserId(userId);
        userRelAppDao.delByUserId(userId);
        userRelOaDepDao.delByUserId(userId);
        this.batchAddIdent(userId,user);
        this.batchAddPbOrg(userId,user);
        this.batchAddDict(userId,user);
        this.batchAddOaOrg(userId,user);
        user.setUpdateTime(new Date(new Date().getTime()));
        userDao.update(user);
    }

    /**
     * 停止启动
     * @param user
     */
    @Override
    @Transactional
    public void switchUser(User user) {
        userDao.switchUser(user);
    }

    /**
     * 重置密码
     * @param id
     */
    @Override
    @Transactional
    public void resetPassword(Long id)  {
        User user = new User();
        //用户id
        user.setId(id);
        //密码盐
        String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);
        //密码+密码盐=新的密码 （存数据库）
        String password=Const.RESET_PASSWORD+salt;
        //md5加密新的密码
        user.setPassword( MD5Util.compute(password));
        userDao.resetPassword(user);
    }

    @Override
    public BaseVo verifyUser(User user) {
        // 根据姓名和身份证号通过接口验证该人是否是党员
        return null;
    }

    @Override
    public BaseVo sendVaildCode(User user) {
        //发送短信验证码
        //todo 目前暂无短信验证码接口，等待短信验证码确定，并确定验证码有效时间
        String phone = String.valueOf(user.getMobile());
        String num = String.valueOf(new Random().nextInt(899999) + 100000);
        cacheUtil.set(Const.REDIS_KEY_SMSCAPTCHA + phone, num, 6000 * 10 * 5);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @Override
    public BaseVo verifyVaildCode(VaildCodeVO vaildCodeVO) {
        String phone = String.valueOf(vaildCodeVO.getMobile());
        //从redis中取验证码
        String code = (String) cacheUtil.get(Const.REDIS_KEY_SMSCAPTCHA + phone);
        //验证验证码是否正确
        if (vaildCodeVO.getVaildCode().equals(code)){
            BaseVo baseVo = new BaseVo();
            return baseVo;
        }else {
            throw new BusinessException("验证码错误");
        }
    }

    @Transactional
    @Override
    public BaseVo partyBuildAdd(User user) {
        int count = userDao.findCountByUsername(user.getMobile());
        if (count > 0){
            throw new BusinessException("用户名重复");
        }else {
            user.setUsername(user.getMobile());
            //随机密码盐
            String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);
            //密码和盐=新密码  md5加密新密码
            String password = MD5Util.compute(user.getPassword() + salt);
            user.setPassword(password);
            user.setSalt(salt);
            user.setInsertTime(new Date());
            user.setStatus(Const.USER_STATUS);
            user.setCreateType(Const.USER_REGISTER);
            userDao.add(user);
            //自增主键id
            Long id = user.getId();

            //存中间表t_sys_user_rel_app
            UserRelApp userRelApp = new UserRelApp();
            userRelApp.setAppId(user.getDictId());
            userRelApp.setUserId(id);
            userRelAppDao.add(userRelApp);
            //存中间表t_sys_user_rel_identity
            Identity identity = new Identity();
            identity.setAppId(user.getDictId());
            identity.setAcquiesce(0);
            Identity identity1 = identityDao.findByAppIdAndAcquiesce(identity);

            UserRelIdentity userRelIdentity = new UserRelIdentity();
            userRelIdentity.setIdentityId(identity1.getId());
            userRelIdentity.setUserId(id);
            userRelIdentityDao.add(userRelIdentity);

            BaseVo baseVo = new BaseVo();
            return baseVo;
        }
    }

    @Override
    @Transactional
    public BaseVo refreshToken(RefreshTokenVO refreshTokenVO) {
        BaseVo baseVo = new BaseVo();
        Map<String,Object> map = new HashMap<>();
        //根据refreshToken和接入端id去redis中查询
        String refreshToken = (String) cacheUtil.get(Const.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
        //如果能取到refreshToken，那么刷新accessToken
        if (StringUtils.isNotEmpty(refreshToken)){
            RefreshTokenVO tokenObject = JSONObject.parseObject(refreshToken, RefreshTokenVO.class);
            if (tokenObject != null){
                String accessToken = (String) cacheUtil.get(tokenObject.getAtKey());
                //如果accessToken不为空，表示token未过期，则删除在新生成token处理
                if (StringUtils.isNotEmpty(accessToken)){
                    //删除
                    cacheUtil.del(tokenObject.getAtKey());
                    //创建新的atToken
                    String atToken = UUIDUtil.getUUID();
                    cacheUtil.set(Const.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken,JSONObject.toJSONString(tokenObject.getUserInfo()),Long.valueOf(atTokenExpire));
                    //refreshToken 更新atKey
                    //获取Key过期时间
                    long expire = cacheUtil.getExpire(Const.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
                    RefreshTokenVO refreshTokenVOParam = new RefreshTokenVO();
                    refreshTokenVOParam.setUserInfo(tokenObject.getUserInfo());
                    refreshTokenVOParam.setAtKey(Const.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken);
                    String rtJson = JSON.toJSONString(refreshTokenVOParam);
                    cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken(), rtJson,expire);
                    map.put("token",atToken);
                    baseVo.setData(map);
                    return baseVo;
                }else {
                    //新增
                    String atToken = UUIDUtil.getUUID();
                    cacheUtil.set(Const.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken,JSONObject.toJSONString(tokenObject.getUserInfo()),Long.valueOf(atTokenExpire));
                    //refreshToken 更新atKey
                    //获取Key过期时间
                    long expire = cacheUtil.getExpire(Const.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken());
                    RefreshTokenVO refreshTokenVOParam = new RefreshTokenVO();
                    refreshTokenVOParam.setUserInfo(tokenObject.getUserInfo());
                    refreshTokenVOParam.setAtKey(Const.TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + atToken);
                    String rtJson = JSON.toJSONString(refreshTokenVOParam);
                    cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + refreshTokenVO.getTerminalId() + ":" + refreshTokenVO.getRefreshToken(), rtJson,expire);
                    map.put("token",atToken);
                    baseVo.setData(map);
                    return baseVo;
                }
            }
        }else {
            throw new BusinessException("当前refreshToken已失效");
        }
        return baseVo;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll("");
    }
}
