package com.mvc.user.rest;

import com.mvc.common.rest.BaseController;
import com.mvc.user.entity.GroupType;
import com.mvc.user.service.GroupTypeBiz;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qyc
 * @create 2017-06-08 11:51
 */
@Controller
@RequestMapping("groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz, GroupType> {

}
