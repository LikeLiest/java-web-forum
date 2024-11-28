import {renderPost} from "./render.js";
import {getCookieValue} from "./cookie.js";

const container = document.querySelector('#render-info')
const div = document.createElement('div')

const article = getCookieValue('article');

if (article === undefined || article === null)
    window.location.href = 'http://localhost:8080/dashboard'

const getPost = await fetch(`http://localhost:8080/about?article=${article}`)
const postResponse = await getPost.json();
const postData = postResponse.data

renderPost(postData, container, div, true);