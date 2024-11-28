import {getCookieValue} from "./cookie.js";

const article = getCookieValue('article')

const addCommentBtn = document.querySelector('#add-comment')

addCommentBtn.onclick = async () => {
    const commentText = document.querySelector('#comment-text').value

    // TODO: ВСЕ ЧТО СВЯЗАНО С ЮЗЕРОМ БРАТЬ ИЗ ТОКЕНА ИЛИ СЕССИИ ИЛИ КУК
    const username = document.querySelector('#username').textContent

    const commentData = {
        username: username,
        content: commentText,
        dateOfCreated: new Date().toLocaleDateString(),
        postArticle: article
    }

    await fetch('http://localhost:8080/comment/addComment', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(commentData)
    })

    window.location.reload()
}

const comments = await getAllComments(article)

async function getAllComments(article) {
    const response = await fetch(`http://localhost:8080/comment/all?article=${article}`)
    const data = await response.json()
    return data.data
}

const commentsList = document.querySelector('#commentsList')

renderComments(comments)

// ЕЩЕ ИКОНКУ ЮЗЕРА ВЫВОДИТЬ
function renderComments(comments) {
    Object.entries(comments).forEach(([key, value]) => {
        const username = value.username
        const content = value.content
        const dateOfCreated = value.dateOfCreated

        const container = document.createElement('li')
        container.innerHTML = `
            <h3>${username}</h3>
            <p>${content}</p>
            <span>${dateOfCreated}</span>
        `
        commentsList.appendChild(container)
    })
}