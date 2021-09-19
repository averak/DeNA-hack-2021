import { useRouter } from "next/router";
import type { VFC } from "react";
import { useEffect } from "react";
import toast, { Toaster } from "react-hot-toast";
import { RegistAuthField } from "src/components/User/AuthField";

import type { SignUpParam } from "../../utils/hooks/userApi";
import { useSignUp as UseSignUp } from "../../utils/hooks/userApi";

const errorPasswordSet = () => {
  return toast.error(
    "パスワードは数字、英大文字、英小文字を含めて８文字以上３２文字以下にしてください。"
  );
};
const errorExistUser = () => {
  return toast.error("ユーザがすでに存在しています。");
};

const UserRegisterPage: VFC = () => {
  const { postFn, response, error } = UseSignUp();

  const router = useRouter();

  useEffect(() => {
    if (!response) {
      return;
    }
    router.push("/");
  }, [response, router]);

  useEffect(() => {
    if (!error) {
      return;
    }

    if (error.message.match(/400/)) {
      errorPasswordSet();
    }

    if (error.message.match(/409/)) {
      errorExistUser();
    }
  }, [error]);

  const authResiter = (
    firstName: string,
    lastName: string,
    mail: string,
    password: string
  ) => {
    const param: SignUpParam = {
      firstName: firstName,
      lastName: lastName,
      email: mail,
      password: password,
    };

    postFn(param);
    return;
  };
  return (
    <div className="flex relative justify-center items-center w-full h-screen bg-blue-500">
      <div className="w-full max-w-[320px] h-auto">
        <RegistAuthField submitText="新規登録" handleSubmit={authResiter} />
        <Toaster />
      </div>
    </div>
  );
};

export default UserRegisterPage;
