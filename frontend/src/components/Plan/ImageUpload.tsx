import type { ChangeEvent } from "react";
import type { VFC } from "react";
import { useEffect, useState } from "react";

type UploadProps = {
  name: string;
  componentRef?: (instance: HTMLInputElement | null) => void;
  image: File | null;
  setImage: (files: File | null) => void;
};

export const ImageUpload: VFC<UploadProps> = ({
  name,
  componentRef,
  image,
  setImage,
}: UploadProps) => {
  const [isFileTypeError, setIsFileTypeError] = useState(false);
  const [imageUrl, setImageUrl] = useState<string | null>(null);

  const handleFile = async (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.files === null || event.target.files.length === 0) {
      return;
    }
    const file = Object.values(event.target.files)[0];
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
    setImage(file);
  };

  const handleCancel = () => {
    if (confirm("選択した画像を消してよろしいですか？")) {
      setIsFileTypeError(false);
    }
  };

  useEffect(() => {
    if (!image) return;
    setImageUrl(URL.createObjectURL(image));
  }, [image]);

  return (
    <>
      <div>
        {imageUrl && (
          <button type="button" onClick={handleCancel}>
            <img src={imageUrl} alt={`投稿された写真`} />
          </button>
        )}
      </div>
      {isFileTypeError && (
        <p>※jpeg, png, bmp, gif, svg以外のファイル形式は表示されません</p>
      )}

      <div>
        <label htmlFor={name}>
          写真を選択(1枚のみ選択可能です)
          <input
            type="file"
            name={name}
            id={name}
            ref={componentRef}
            accept="image/*"
            onChange={handleFile}
            multiple
            className="pt-3"
          />
        </label>
      </div>
    </>
  );
};
