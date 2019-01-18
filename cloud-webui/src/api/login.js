import request from '../utils/request';

/**
 * 登陆
 * @param username
 * @param password
 * @returns {*}
 */
export function login (username, password) {
    var grant_type = 'password';
    var scope = 'webapp';
    return request({
        url: '/oauth/token',
        method: 'post',
        headers: {
            'Authorization': 'Basic Y2xvdWQ6Y2xvdWQ=' // clientId:clientSecret => base64
        },
        params: { username, password, grant_type, scope }
    });
}

/**
 * 获取用户信息
 * @returns {*}
 */
export function getInfo () {
    return request({
        url: '/uc/user/info',
        method: 'get'
    });
}

/**
 * 修改密码
 * @param oldPassword
 * @param newPassword
 * @returns {*}
 */
export function updatePassword (oldPassword, newPassword) {
    return request({
        url: '/uc/user/updatePwd',
        method: 'put',
        params: { oldPassword, newPassword }
    });
}

/**
 * 登出
 * @param accessToken
 * @param refreshToken
 * @returns {*}
 */
export function logout (accessToken, refreshToken) {
    return request({
        url: '/oauth/removeToken',
        method: 'post',
        params: { accessToken, refreshToken }
    });
}
