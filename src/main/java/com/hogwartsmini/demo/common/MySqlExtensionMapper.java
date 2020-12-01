package com.hogwartsmini.demo.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MySqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
    //泛型T，就是个业务对象，例如查询是个用户信息，T就是用户类型
}
