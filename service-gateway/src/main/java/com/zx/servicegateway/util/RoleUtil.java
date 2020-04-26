package com.zx.servicegateway.util;

import org.apache.shiro.subject.Subject;

public class RoleUtil {

    public static String getRole(Subject subject) {
        if (subject.hasRole("manager")) {
            return "manager";
        }
        if (subject.hasRole("accountant")) {
            return "accountant";
        }
        if (subject.hasRole("overallchief")) {
            return "overallchief";
        }
        if (subject.hasRole("areachief")) {
            return "areachief";
        }
        if (subject.hasRole("buyer")) {
            return "buyer";
        }
        if (subject.hasRole("operator")) {
            return "operator";
        }
        return "saler";
    }
}
