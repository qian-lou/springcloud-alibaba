package com.zeny.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName Payment
 * @Description TODO
 * @Author zeny
 * @Date 2020/4/2 0002 21:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment implements Serializable {

    private Long id;

    private String serial;
}
