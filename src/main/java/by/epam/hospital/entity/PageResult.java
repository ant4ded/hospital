package by.epam.hospital.entity;

import java.util.List;

public class PageResult<T> {
    private final int total;
    private final List<T> list;

    private PageResult(List<T> list, int total) {
        this.list = list;
        this.total = total;
    }

    public static <T> PageResult<T> from(List<T> list, int total) {
        return new PageResult<>(list, total);
    }

    public int getTotal() {
        return total;
    }

    public List<T> getList() {
        return list;
    }
}
