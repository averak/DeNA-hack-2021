import { Menu } from '@headlessui/react'
import type { VFC } from "react";
import type { FormEvent } from "react";
import { useState } from "react";
import { useEffect } from "react";

const SearchPage: VFC = () => {
  const [prefecture, setPrefecture] = useState<string>("")
  const [minPrice, setMinPrice] = useState<number>(0)
  const [maxPrice, setMaxPrice] = useState<number>(0)
  const [tags, setTags] = useState<string[]>([])
  
  const [isOpenPrefectureDrop, setIsOpenPrefectureDrop] = useState<boolean>(false)

  useEffect(() => {
    setPrefecture("東京")
    setMinPrice(10000)
    setMaxPrice(50000)
    setTags(["清水寺", "スカイツリー"])
  }, [])

  const changeMinPrice = (e: FormEvent<HTMLInputElement>) => {
    setMinPrice(parseInt(e.currentTarget.value))
  }

  const changeMaxPrice = (e: FormEvent<HTMLInputElement>) => {
    setMaxPrice(parseInt(e.currentTarget.value))
  }

  const changeTags = (e: FormEvent<HTMLInputElement>) => {
    setTags([e.currentTarget.value])
  }

  const clickPrefectureDropDown = () => {
    setIsOpenPrefectureDrop(!isOpenPrefectureDrop)
  }

  const prefectureData: string[] = ["北海道", "沖縄"];

  return (
  <main>
    <div>
      <div className="bg-blue-600 text-center pb-3 shadow-lg">

        <button className="m-3 text-left bg-white rounded-lg px-2 py-1.5 w-3/4 font-bold text-sm inline-flex justify-between" onClick={clickPrefectureDropDown}>
          {prefecture}
          <p className="ml-2">v</p>
        </button>

        {isOpenPrefectureDrop && (
                    <div className="origin-top-right absolute right-0 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none" aria-orientation="vertical" aria-labelledby="menu-button" tabIndex={-1}>
                      <div className="py-1" role="none">
                        {
                          prefectureData.map((prefecture) => {
                            return <button className="text-gray-700 block px-4 py-2 text-sm">{prefecture}</button>
                          })
                        }
                        <p className="text-gray-700 block px-4 py-2 text-sm" tabIndex={-1} >ccount settings</p>
                        <p className="text-gray-700 block px-4 py-2 text-sm" tabIndex={-1}>Account settings</p>
                        <p className="text-gray-700 block px-4 py-2 text-sm" tabIndex={-1}>Account settings</p>
                      </div>
                    </div>
                )}

        <div className="flex justify-center">
          <input className="m-3 rounded-lg px-2 py-1.5 w-1/4 font-bold text-right text-sm" type="text" placeholder="指定なし" value={minPrice} onChange={changeMinPrice}></input>
          <p className="text-white text-xl pt-2.5">~</p>
          <input className="m-3 rounded-lg px-2 py-1.5 w-1/4 font-bold text-right  text-sm" type="text" placeholder="指定なし" value={maxPrice} onChange={changeMaxPrice}></input>
          <p className="text-white text-sm pt-6">円</p>
        </div>

        <input className="m-3 rounded-lg px-2 py-1.5 w-3/4" type="text" placeholder="なし" value={tags} onChange={changeTags}/>
      </div>

      <p className="font-bold text-center text-lg m-4">検索結果</p>
    </div>
  </main>
  );
};

export default SearchPage;
