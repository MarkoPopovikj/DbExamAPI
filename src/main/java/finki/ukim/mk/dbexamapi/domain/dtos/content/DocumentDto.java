package finki.ukim.mk.dbexamapi.domain.dtos.content;

import finki.ukim.mk.dbexamapi.domain.models.Folder;

public record DocumentDto(
        Folder folder,
        String name,
        String documentMd
) {
}
