import {renderPost, renderPostObject} from "./renderAllPosts.js";

const searchResultContainer = document.querySelector('#render-search-result')

const searchPostByID = document.querySelector('#search-post')
const searchPostByTitle = document.querySelector('#search-post-by-title')

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

    timeout = setTimeout(async () => {
            let post

            if (/^\d+$/.test(searchPost)) {
                post = await fetchToSearchPostByIdentifier(Number(searchPost))
                console.log(post)
            } else {
                post = await fetchToSearchPostByIdentifier(searchPost)
                console.log(post)
            }
            const p = document.createElement('p')

            if (post !== undefined)
                renderPostObject(post, searchResultContainer, p)

        }, 500
    )
}

const renderError = error => {
    searchResultContainer.innerHTML = ``
    searchResultContainer.textContent = error
}

const fetchToSearchPostByIdentifier = async identifier => {
    const response = await fetch(`posts/${identifier}`)

    if (!response.ok) {
        renderError(`Пост не найден`)
        return
    }

    return await response.json();
}
