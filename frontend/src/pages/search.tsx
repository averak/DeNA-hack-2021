import { Menu } from "@headlessui/react"
import { XIcon } from "@heroicons/react/solid"
import type { VFC } from "react";
import type { FormEvent } from "react";
import { useState } from "react";
import { useEffect } from "react";

const SearchPage: VFC = () => {
  const [prefecture, setPrefecture] = useState<string>("")
  const [minPrice, setMinPrice] = useState<number>(0)
  const [maxPrice, setMaxPrice] = useState<number>(0)
  const [tags, setTags] = useState<string[]>([])
  const [inputTag, setInputTag] = useState<string>("")
  
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

  const changeInputTag = (e: FormEvent<HTMLInputElement>) => {
    setInputTag(e.currentTarget.value)
  }

  const clickPrefectureDropDown = () => {
    setIsOpenPrefectureDrop(!isOpenPrefectureDrop)
  }

  const prefectures: string[] = ["山形", "神奈川"]

  const changePrefecture = (e: FormEvent<HTMLButtonElement>) => {
    if(e.currentTarget.textContent){
      setPrefecture(e.currentTarget.textContent)
    }
  }

  const addTagToTags = (addtag: string) => {
    const newTags: string[] = tags.concat()
    newTags.push(addtag)
    setTags(newTags)
    setInputTag("")
  }

  const removeTagFromTags = (removeTag: string) => {
    setTags(tags.filter((tag) => {
      if(tag !== removeTag){
        return tag
      }
    }))
}

  return (
  <main>
    <div>
      <div className="pb-3 text-center bg-blue-600 shadow-lg">
        <Menu>
          <Menu.Button className="inline-flex justify-between py-1.5 px-2 m-3 w-3/4 text-sm font-bold text-left bg-white rounded-lg" onClick={clickPrefectureDropDown}>{prefecture}</Menu.Button>
          <Menu.Items>
            {prefectures.map((prefecture, i) => {
              return (
                <Menu.Item key={i}>
                    <button onClick={changePrefecture}>
                      {prefecture}
                    </button>
                </Menu.Item>
              )
            })
          }
          </Menu.Items>
        </Menu>

        <div className="flex justify-center">
          <input className="py-1.5 px-2 m-3 w-1/4 text-sm font-bold text-right rounded-lg" type="text" placeholder="指定なし" value={minPrice} onChange={changeMinPrice}/>
          <p className="pt-2.5 text-xl text-white">~</p>
          <input className="py-1.5 px-2 m-3 w-1/4 text-sm font-bold text-right rounded-lg" type="text" placeholder="指定なし" value={maxPrice} onChange={changeMaxPrice}/>
          <p className="pt-6 text-sm text-white">円</p>
        </div>

        <div className="flex justify-center">
          <input className="py-1.5 px-2 m-3 w-7/12 text-sm font-bold text-left bg-white rounded-lg" type="text" placeholder="タグ検索" value={inputTag} onChange={changeInputTag}/>
          <button className="py-1 px-2 my-3 text-sm font-bold text-left bg-white rounded-lg" onClick={() => {addTagToTags(inputTag)}}>追加</button>
        </div>

        <div className="">
          { tags.map((tag, i) => {
            return (
            <div key={i} className="inline-flex items-center py-1 px-3 ml-4 text-xs font-bold text-gray-700 bg-white rounded-full border">
              <button onClick={() => {removeTagFromTags(tag)}}>
                <XIcon className="w-4"/>
              </button>
              {tag}
            </div>)
            })
          }
        </div>
      </div>

      <p className="m-4 text-lg font-bold text-center">検索結果</p>

      {/* write componets */}

    </div>
  </main>
  );
};

export default SearchPage;
