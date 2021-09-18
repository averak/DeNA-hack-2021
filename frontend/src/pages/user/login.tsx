import { useRouter } from "next/router";
import type { VFC } from "react";
import { useEffect } from "react";
import { LoginAuthField } from "src/components/User/AuthField";

import type { LoginParam } from "../../utils/hooks/userApi";
import { useLogin as UseLogin } from "../../utils/hooks/userApi";

const LoginPage: VFC = () => {
  const { postFn, response, error } = UseLogin();

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
  }, [error]);

  const authLogin = (mail: string, password: string) => {
    const param: LoginParam = {
      email: mail,
      password: password,
    };
    postFn(param);
    return;
  };
  return (
    <div className="flex relative justify-center items-center w-full h-screen bg-blue-500">
      <div className="w-full max-w-[320px] h-auto">
        <LoginAuthField submitText="ログイン" handleSubmit={authLogin} />
      </div>
    </div>
  );
};

export default LoginPage;
