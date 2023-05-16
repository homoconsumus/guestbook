package com.example.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    //DTO목록
    private List<DTO> dtoList;
    //전체 페이지수
    private int totalPage;
    //현재 페이지번호
    private int page;
    //한 페이지의 사이즈
    private int size;
    //시작페이지번호, 끝페이지번호(한 화면의)
    private int start, end;
    //이전, 다음 존재여부(한 화면 단위)  화면>페이지>행
    private boolean prev, next;
    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages(); // 전체 페이지번호 개수 = 테이블의 Entity 개수
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();
        // 한 화면의 임시 마지막 페이지 번호
        int tempEnd = (int)Math.ceil(page/10.0)*10;
        // 한 화면의 처음 시작 번호
        start = tempEnd - 9;
        // 맨 마지막 화면 최종 end 페이지 번호
        end = totalPage > tempEnd ? tempEnd : totalPage;
        // 화면의 이전, 다음 존재 여부
        prev = start > 1;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
