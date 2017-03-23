package com.liuruichao.dto;

import com.liuruichao.model.EnrollResult;
import lombok.Data;

/**
 * EnrollResponse
 *
 * @author liuruichao
 * Created on 2017/3/23 14:27
 */
@Data
public class EnrollResponse {
    private boolean success;

    private EnrollResult result;

}
