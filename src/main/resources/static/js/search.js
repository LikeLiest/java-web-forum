import {renderPost, renderPostObject} from "./render.js";

const searchResultContainer = document.querySelector('#render-search-result')

export const searchPostByID = document.querySelector('#search-post')
export const searchPostByTitle = document.querySelector('#search-post-by-title')

let timeout;

searchPostByID.oninput = async () => {
    renderSearchResult(searchPostByID.value)
}

searchPostByTitle.oninput = async () => {
    renderSearchResult(searchPostByTitle.value)
}

function renderSearchResult(inputValue) {
    clearTimeout(timeout)
    searchResultContainer.innerHTML = ``

    const searchPost = inputValue.trim()

    if (searchPost === '')
        return

    timeout = setTimeout(async () => {
            const p = document.createElement('p')

            if (/^\d+$/.test(searchPost)) {
                const post = await fetchToSearchPostByIdentifier(Number(searchPost))

                if (post !== undefined)
                    renderPost(post, searchResultContainer, p)

                console.log(post)
            } else {
                const post = await fetchToSearchPostByIdentifier(searchPost)

                if (post !== undefined)
                    renderPostObject(post, searchResultContainer, p)

                console.log(post)
            }
        }, 500
    )
}

const renderError = error => {
    searchResultContainer.innerHTML = ``
    searchResultContainer.textContent = error
}

const fetchToSearchPostByIdentifier = async identifier => {
    const response = await fetch(`http://localhost:8080/posts/${identifier}`)

    if (!response.ok) {
        renderError(`Пост не найден`)
        return
    }

    return await response.json();
}
