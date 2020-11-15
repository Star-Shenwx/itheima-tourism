package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 用户保存功能
     * @param user
     */
    public void save(User user);

    /**
     * 根据用户激活码查找用户信息
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 更新用户的激活状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码查询用户信息
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
