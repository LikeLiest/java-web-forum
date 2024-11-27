import {renderPost} from "./render.js";
import {getCookieValue} from "./cookie.js";

const container = document.querySelector('#render-info')
const p = document.createElement('p')

const article = getCookieValue('article');
const getPost = await fetch(`http://localhost:8080/about?article=${article}`)
const postData = await getPost.json();

renderPost(postData, container, p);