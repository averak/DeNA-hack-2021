import { MailIcon, UserIcon } from "@heroicons/react/outline";
import type { ReactNode, VFC } from "react";
import styles from "src/styles/AuthField.module.css";

type InputProps = {
  icon: ReactNode;
  placeholder: string;
};
const InputField: VFC<InputProps> = ({ icon, placeholder }) => {
  return (
    <div className="flex mt-6 h-14">
      <div className="flex justify-center items-center w-16 rounded-tl-lg rounded-bl-lg border border-gray-200 border-solid">
        {icon}
      </div>
      <input
        className="pl-2 w-full h-full rounded-tr-lg rounded-br-lg outline-none"
        placeholder={placeholder}
      />
    </div>
  );
};

type AuthProps = {
  submitText: string;
  handleSubmit: () => void;
};
export const AuthField: VFC<AuthProps> = ({ submitText, handleSubmit }) => {
  return (
    <div className="w-full h-auto">
      <div className="flex gap-3 justify-center items-center pb-6">
        <img
          src="/trippad_icon.svg"
          width={50}
          height={50}
          alt="トリップパッドのアイコン"
        />
        <p className="text-4xl font-bold text-white">trippad</p>
      </div>
      <InputField
        icon={<UserIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="ユーザーネーム"
      />
      <InputField
        icon={<MailIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="メールアドレス"
      />
      <div className="pt-8">
        <button
          className={`block mx-auto w-[220px] h-12 font-bold bg-white rounded ${styles.submitButton}`}
          onClick={handleSubmit}
        >
          {submitText}
        </button>
      </div>
    </div>
  );
};
