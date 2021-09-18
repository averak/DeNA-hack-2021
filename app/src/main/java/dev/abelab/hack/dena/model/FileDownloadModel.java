package dev.abelab.hack.dena.model;

import lombok.*;

/**
 * ファイルのダウンロードモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadModel {

    /**
     * ファイル名
     */
    String fileName;

    /**
     * ファイル内容
     */
    byte[] content;

}
