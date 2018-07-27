package com.love.baby.web;

import com.love.baby.web.vo.UserVo;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by liangbc on 2018/7/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest extends BaseRestDoc {
    @Test
    public void userInfo() throws Exception {
        MvcResult resul = this.mockMvc.perform(get("/user/userInfo").header("x-auth-token", token).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(CoreMatchers.instanceOf(UserVo.class)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
//        mockMvc.perform(asyncDispatch(resul))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andDo(document("查询用户",
//                        pathParameters(parameterWithName("id").description("用户id")),
//                        relaxedResponseFields(
//                                fieldWithPath("id").type("String").description("城市id")
//                        )
//                        )
//                );

    }
}
