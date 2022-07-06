package com.project.simplegw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.simplegw.document.approval.dtos.receive.docs.DtorDefaultReport;

@SpringBootTest
@AutoConfigureMockMvc
public class ApprovalTest {

    @Autowired
    private MockMvc mvc;


    private void get(String urlString) throws Exception {
        mvc
        .perform(
            MockMvcRequestBuilders
            .get(urlString)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void post(Object paramObj, String urlString) throws Exception {
        String params = new ObjectMapper().writeValueAsString(paramObj);

        mvc
        .perform(
            MockMvcRequestBuilders
            .post(urlString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(params)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void patch(Object paramObj, String urlString) throws Exception {
        String params = new ObjectMapper().writeValueAsString(paramObj);

        mvc
        .perform(
            MockMvcRequestBuilders
            .patch(urlString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(params)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void patchNoParams(String urlString) throws Exception {
        mvc
        .perform(
            MockMvcRequestBuilders
            .patch(urlString)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void delete(String urlString) throws Exception {
        mvc
        .perform(
            MockMvcRequestBuilders
            .delete(urlString)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());
    }





    @Test
    @WithUserDetails(value = "developer")
    void approvalCreate() throws Exception {   // 결재 승인, 반려 테스트를 위한 기본 결재문서 등록
        DtorDefaultReport dto = new DtorDefaultReport().setTitle("test").setContent("test").setArrApproverId( new Long[]{122L, 70L, 23L, 65L} );
        post(dto, "/approval/default");
    }



    @Test
    @WithUserDetails(value = "")
    void approvalConfirmed() throws Exception {
        patchNoParams("/approval/confirmed/DEFAULT/37733");
    }
}
