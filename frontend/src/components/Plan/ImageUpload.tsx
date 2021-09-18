import type { ChangeEvent } from "react";
import type { VFC } from "react";
import { useState } from "react";

type UploadProps = {
  name: string;
  componentRef?: (instance: HTMLInputElement | null) => void;
  image: File;
  setImage: (files: File) => void;
};

export const ImageUpload: VFC<UploadProps> = ({
  name,
  componentRef,
  image,
  setImage,
}: UploadProps) => {
  const [isFileTypeError, setIsFileTypeError] = useState(false);

  const handleFile = async (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.files === null || event.target.files.length === 0) {
      return;
    }
    const files = Object.values(event.target.file).concat();
    // 初期化することで同じファイルを連続で選択してもonChagngeが発動するように設定し、画像をキャンセルしてすぐに同じ画像を選ぶ動作に対応
    event.target.value = "";
    setIsFileTypeError(false);

    if (
      ![
        "image/gif",
        "image/jpeg",
        "image/png",
        "image/bmp",
        "image/svg+xml",
      ].includes(file.type)
    ) {
      setIsFileTypeError(true);
    }

    setImage();
  };

  const handleCancel = () => {
    if (confirm("選択した画像を消してよろしいですか？")) {
      setIsFileTypeError(false);
      setImage(null);
    }
  };

  return (
    <>
      <div>
        <button type="button" onClick={handleCancel}>
          <img src={URL.createObjectURL(image)} alt={`投稿された写真`} />
        </button>
      </div>
      {isFileTypeError && (
        <p>※jpeg, png, bmp, gif, svg以外のファイル形式は表示されません</p>
      )}

      <div>
        <label htmlFor={name}>
          写真を選択
          <input
            type="file"
            name={name}
            id={name}
            ref={componentRef}
            accept="image/*"
            onChange={handleFile}
            multiple
          />
        </label>
      </div>
    </>
  );
};
