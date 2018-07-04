package com.along101.contract.common.utils;

import com.along101.atlas.model.PageDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * convert between Entity and Dto
 * <p>
 * Created by qiankai02 on 2017/11/8.
 */
public class ConvertUtils {

    /**
     * copy properties from s to t
     *
     * @param s   source instance
     * @param t   dest instance
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T convert(S s, T t) {
        try {
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (BeansException e) {
            //Todo throw new Exception();
            throw new RuntimeException(e);
        }
    }

    public static <S, T> T convert(S s, T t, String... ignoreProperties) {
        try {
            BeanUtils.copyProperties(s, t, ignoreProperties);
            return t;
        } catch (BeansException e) {
            //Todo throw new Exception();
            throw new RuntimeException(e);
        }
    }

    public static <S, T> T convert(S s, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            BeanUtils.copyProperties(s, t);
            return t;
        } catch (Exception e) {
            //Todo throw new Exception();
            throw new RuntimeException(e);
        }
    }

    public static <S, T> List<T> convert(Iterable<S> sIterable, Function<? super S, ? extends T> mapper) {
        return StreamSupport.stream(sIterable.spliterator(), false).map(mapper).collect(Collectors.toList());
    }

    public static <S, T> List<T> convert(Iterable<S> sIterable, Class<T> tClass) {
        return convert(sIterable, a -> convert(a, tClass));
    }

/*    public static <S, T> PageDto convertPage(Page<S> page, Function<? super S, ? extends T> mapper) {
        List<S> contents = page.getContent();
        List tContents = convert(contents, mapper);
        PageDto pageDto = new PageDto();
        pageDto.setContent(tContents);
        pageDto.setFirst(page.isFirst());
        pageDto.setLast(page.isLast());
        pageDto.setNumber(page.getNumber());
        pageDto.setSize(page.getSize());
        pageDto.setNumberOfElements(page.getNumberOfElements());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setTotalPages(page.getTotalPages());
        return pageDto;
    }

    public static <S, T> PageDto convertPage(Page<S> page, Class<T> tClass) {
        return convertPage(page, a -> convert(a, tClass));
    }*/

}
