package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

        @Autowired
        private DishMapper dishMapper;

        @Autowired
        private DishFlavorMapper dishFlavorMapper;

        @Transactional(rollbackFor = Exception.class)
        public void saveWithFlavor(DishDTO dishDTO) {
            Dish dish = new Dish();
            BeanUtils.copyProperties(dishDTO, dish);

            // 向菜品表插入一条数据
            dishMapper.insert(dish);

            // 获取insert语句生成的主键
            Long dishId = dish.getId();

            List<DishFlavor> flavors = dishDTO.getFlavors();
            if (flavors != null && !flavors.isEmpty()) {
                flavors.forEach(dishFlavor -> {
                    dishFlavor.setDishId(dishId);
                });
                // 向菜品口味表插入多条数据
                dishFlavorMapper.insertBatch(flavors);
            }

        }
}
