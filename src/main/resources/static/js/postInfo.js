import {renderPost} from "./renderAllPosts";

async function getPostData() {
    const response = await fetch('post/about')
    return await response.json();
}

const post = await getPostData();

const renderContainer = document.querySelector('#render-info')
const p = document.createElement('p')

renderPost(post, renderContainer, p)