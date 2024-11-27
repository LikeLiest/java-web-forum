import {renderPost} from "./render.js";

await renderAllPosts()

async function renderAllPosts() {
    const posts = await getAllPosts();
    const postContainer = document.querySelector('#post-container');

    posts.forEach(post => {
        const ul = document.createElement('ul');
        const li = document.createElement('li')

        renderPost(post, ul, li)

        postContainer.appendChild(ul);
    });
}


async function getAllPosts() {
    const response = await fetch('http://localhost:8080/posts/all');
    return await response.json()
}

// TODO -> Посмотреть на такую логику:
// TODO -> Рендерить только первое изображение в большом формате, а все остальные изображения(если присутствуют) добавлять как маленькие изображения