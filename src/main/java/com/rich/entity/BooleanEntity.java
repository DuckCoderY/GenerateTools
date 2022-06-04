package com.rich.entity;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: l_y
 * @Date: 2022/6/2 16:40
 * @FileName: BooleanEntity
 * @Description:
 */
@Data
public class BooleanEntity {
    AtomicBoolean beenBoolean = new AtomicBoolean(false);
    AtomicBoolean convertBoolean = new AtomicBoolean(false);
    AtomicBoolean requestBoolean = new AtomicBoolean(false);
}
