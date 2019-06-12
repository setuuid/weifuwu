package com.bit.common;
/**
 * @Description: 静态变量
 * @Author: liqi
 **/
public class Const {

    /**
     *  根节点的PID 根级父亲id
     */
    public static final long ROOT_RESOURCE_PID = 0L;
    /**
     *  党建组织表 根节点的PID 根级父亲id
     */
    public static final long PB_PID = 0L;
    /**
     * 重置密码
     */
    public static final String RESET_PASSWORD = "123456";
    /**
     * 密码盐 （6位）
     */
    public static final int RANDOM_PASSWORD_SALT = 6;
    /**
     * 第一个二进制 党组织id
     */
    public static final String BINARY = "0000001000000000000000000000000000000000000000000000000000000000";
    /**
     * 第一个8位--拼装二进制
     */
    public static final String FIRST_EIGHT_BINARY = "0000001";


    public static final String TOKEN_PREFIX = "token:";
    /**
     * 刷新token
     */
    public static final String REFRESHTOKEN_TOKEN_PREFIX = "refreshToken:";

    /**
     * 字典redis key 短信验证码
     */
    public static final String REDIS_KEY_SMSCAPTCHA = "smsCaptcha:";

    /**
     * 保存用户默认账号状态
     */
    public static final Integer USER_STATUS = 1;

    /**
     * 平台创建
     */
    public static final Integer USER_CREATE_TYPE = 1;

    /**
     * app注册
     */
    public static final Integer USER_REGISTER = 2;

    /**
     * 角色设置菜单 1代表已设置
     */
    public static final int ALREADYSET = 1;
}
