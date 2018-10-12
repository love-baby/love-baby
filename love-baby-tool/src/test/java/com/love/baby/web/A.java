package com.love.baby.web;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by liangbc on 2018/8/24.
 */
public class A {
    public static void main(String[] args) {
        String sql = "8a9d24ce5ec76e84015ecc9f142114f9,8a9d24ce5ec76e84015eccb791731605,8a9d24ce5ed240c9015f046ff2282382,8a9d24ce5ed240c9015f0ec9511956f4,8a9d24ce5f109612015f1514dc321669,8a9e3f905ec76d2a015eccc3f78f1661,8a9e3f905ed1c0d5015f0eca60a65903";

        String[] arrs = StringUtils.split(sql, ",");

        for (int i = 0; i < arrs.length; i++) {
            System.out.println("INSERT INTO `goabroad`.`apply_balance` (`id`, `seeker_apply_id`, `apply_receipt_id`, `price`, `status`, `balance_time`, `describe`, `user_id`, `recruiter_user_id`, `create_time`, `modify_time`) VALUES ('" + arrs[i] + "', '" + arrs[i] + "', '" + arrs[i] + "', '500.00', '3', '2018-08-20 17:21:15', '老数据，统一修正', '8a1026775acb8297015acc365a410d4b', '8a1026775acb8297015acc365a410d4b', '2017-10-30 13:50:24', '2017-10-30 13:50:24');");
        }

    }
}
