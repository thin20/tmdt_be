package com.example.tmdt_be.service.sdi;

import lombok.*;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class MediaFileSdi {
    private String filePath;
    private String fileType;
    private long fileLength;
}
