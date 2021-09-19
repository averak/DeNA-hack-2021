import { KeyIcon, MailIcon, UserIcon } from "@heroicons/react/outline";
import Link from "next/link";
import type {
  Dispatch,
  FormEvent,
  ReactNode,
  SetStateAction,
  VFC,
} from "react";
import { useState } from "react";
import styles from "src/styles/AuthField.module.css";

type InputProps = {
  icon: ReactNode;
  placeholder: string;
  setValue: Dispatch<SetStateAction<string>>;
  isPassword: boolean;
};
const InputField: VFC<InputProps> = ({
  icon,
  placeholder,
  setValue,
  isPassword,
}) => {
  const [inputValue, setInputValue] = useState<string>("");

  const changeInputValue = (e: FormEvent<HTMLInputElement>) => {
    setInputValue(e.currentTarget.value);
    setValue(e.currentTarget.value);
  };

  return (
    <div className="flex mt-6 h-14">
      <div className="flex justify-center items-center w-16 rounded-tl-lg rounded-bl-lg border border-gray-200 border-solid">
        {icon}
      </div>
      <input
        className="pl-2 w-full h-full rounded-tr-lg rounded-br-lg outline-none"
        placeholder={placeholder}
        value={inputValue}
        onChange={changeInputValue}
        type={isPassword ? "password" : "text"}
      />
    </div>
  );
};

type LoginAuthProps = {
  submitText: string;
  handleSubmit: (mail: string, password: string) => void;
};

export const LoginAuthField: VFC<LoginAuthProps> = ({
  submitText,
  handleSubmit,
}) => {
  const [mail, setMail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

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
        icon={<MailIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="メールアドレス"
        setValue={setMail}
        isPassword={false}
      />
      <InputField
        icon={<KeyIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="パスワード"
        setValue={setPassword}
        isPassword={true}
      />
      <div className="pt-8">
        <button
          className={`block mx-auto w-[220px] h-12 font-bold bg-white rounded ${styles.submitButton}`}
          onClick={() => {
            handleSubmit(mail, password);
          }}
        >
          {submitText}
        </button>
      </div>
      <div className="pt-8">
        <Link href="/user/register">
          <button
            className={`block mx-auto w-[220px] h-12 font-bold bg-white rounded ${styles.submitButton}`}
          >
            新規登録画面へ
          </button>
        </Link>
      </div>
    </div>
  );
};

type RegistAuthProps = {
  submitText: string;
  handleSubmit: (
    firstName: string,
    lastName: string,
    mail: string,
    password: string
  ) => void;
};

export const RegistAuthField: VFC<RegistAuthProps> = ({
  submitText,
  handleSubmit,
}) => {
  const [lastName, setLastName] = useState<string>("");
  const [firstName, setFirstName] = useState<string>("");
  const [mail, setMail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

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
        placeholder="姓"
        setValue={setLastName}
        isPassword={false}
      />
      <InputField
        icon={<UserIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="名"
        setValue={setFirstName}
        isPassword={false}
      />
      <InputField
        icon={<MailIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="メールアドレス"
        setValue={setMail}
        isPassword={false}
      />
      <InputField
        icon={<KeyIcon className="w-9 h-9 text-white stroke-1" />}
        placeholder="パスワード"
        setValue={setPassword}
        isPassword={true}
      />
      <div className="pt-8">
        <button
          className={`block mx-auto w-[220px] h-12 font-bold bg-white rounded ${styles.submitButton}`}
          onClick={() => {
            handleSubmit(firstName, lastName, mail, password);
          }}
        >
          {submitText}
        </button>
      </div>
    </div>
  );
};
