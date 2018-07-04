package com.along101.contract.controller;

import com.along101.contract.vo.ProtoFileVO;
import com.along101.contract.util.JsonToObjectUtil;
import com.along101.contract.vo.ProtoFileVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProtoFileControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testContractController() throws Exception {

        ProtoFileVO protoFileVO = JsonToObjectUtil.getTestObject("service/ContractDto.json", ProtoFileVO.class);

/*        this.mockMvc.perform(post("/web/contracts/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(protoFileVO)))
                .andExpect(status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().string("create success"))
                .andReturn();*/

        this.mockMvc.perform(get("/web/contracts/15"))
                .andExpect(status().is(200))
                .andDo(MockMvcResultHandlers.print())
                //.andExpect(content().string("book"))
                .andReturn();
    }


}
