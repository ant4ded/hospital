package by.epam.hospital.entity;

import java.util.List;

public class PageResult<T> {
    private final int totalPages;
    private final List<T> list;

    private PageResult(List<T> list, int totalPages) {
        this.list = list;
        this.totalPages = totalPages;
    }

    public static <T> PageResult<T> from(List<T> list, int totalPages) {
        return new PageResult<>(list, totalPages);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getList() {
        return list;
    }
}
