package com.example.tmdt_be.service.sdo;

import com.example.tmdt_be.service.sdi.MediaFileSdi;
import lombok.*;

import java.util.List;

@Setter
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class AmazonUploadSdo {
    private List<MediaFileSdi> filePath;
}
