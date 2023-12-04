package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询对应的套餐id
     * @param dishIds 菜品id集合
     * @return 套餐id集合
     */
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);

    /**
     * 更新套餐菜品表
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 批量插入套餐菜品表
     * @param setmealDishes
     */
    @AutoFill(OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐包含的菜品
     * @param SetmealId
     * @return
     */
    @Select("select * from setmeal_dish where dish_id = #{SetmealId}")
    List<SetmealDish> getBySetmealId(Long SetmealId);

    /**
     * 根据setmealId删除对应菜品
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmeal_id}")
    void deleteBySetmealId(Long setmealId);
}
