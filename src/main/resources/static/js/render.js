import {setCookie} from "./cookie.js";

export function renderPost(post, container, htmlElement) {
    if (post.data !== undefined) {
        const postData = post.data
        renderPostData(postData, container, htmlElement)
    }

    console.log(post)
    renderPostData(post, container, htmlElement)
    renderButtonForDeletePost(container, post.id)
    renderAboutPostLink(post, container)
}

export function renderPostData(post, container, htmlElement) {
    Object.entries(post).forEach(([key, value]) => {
        if (key === 'commentList' && Array.isArray(value)) {
            renderPostComments(container, value);

        } else if (key === 'imageList' && Array.isArray(value)) {
            renderPreviewImage(container, value)

        } else {
            renderDefaultPostProps(container, key, value, htmlElement)
        }
    });
}

export function renderPostObject(post, container, htmlElement) {
    const arraysFromObject = post.data

    arraysFromObject.forEach(object => {
        Object.entries(object).forEach(([key, value]) => {
            if (key === 'commentList' && Array.isArray(value)) {
                renderPostComments(container, value);

            } else if (key === 'imageList' && Array.isArray(value)) {
                const base64 = value[0].base64
                renderPreviewImageForTitle(container, base64)
            } else {
                renderDefaultPostProps(container, key, value, htmlElement)
            }
        });
        renderButtonForDeletePost(container, post.id)
        renderAboutPostLink(container, "test")
    })

    console.log(arraysFromObject)
}

export function renderAboutPostLink(post, container) {
    const a = document.createElement('a');
    a.href = `http://localhost:8080/info`;
    a.textContent = 'Посмотреть подробней';

    a.onclick = () => {
        const postArticle = post.article
        setCookie('article', postArticle);
    }

    container.appendChild(a);
}

export function renderDefaultPostProps(container, postKey, postValue, htmlElement) {
    if (postKey !== 'id' && postKey !== "article") {
        if (postKey === 'dateOfCreate') {
            const dateOfCreate = postValue.split('-');
            dateOfCreate[0] = dateOfCreate[0].slice(2);

            const formattedDateOfCreate = dateOfCreate.join(' ')

            const span = document.createElement('span')
            span.textContent = formattedDateOfCreate

            htmlElement.appendChild(span);
        } else {
            const span = document.createElement('span')
            span.textContent = postValue
            htmlElement.appendChild(span);
        }
    }

    container.appendChild(htmlElement);
}

export function renderButtonForDeletePost(htmlElement, id) {
    const button = document.createElement('button');
    button.type = 'submit';
    button.textContent = 'Удалить пост';
    button.onclick = () => deleteThisPost(id);
    htmlElement.appendChild(button);
}

export async function deleteThisPost(id) {
    await fetch(`http://localhost:8080/posts/${id}`, {method: 'DELETE'});
    document.cookie = `article=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
    window.location.reload();
}

export function renderPreviewImage(container, postImages) {
    const previewImage = postImages[0];

    const div = document.createElement('div')
    const img = document.createElement('img');
    img.src = `data:image/jpg;base64,${previewImage.base64} ` || '';
    img.alt = previewImage.alt || 'Image';
    div.appendChild(img)
    container.appendChild(div);
}

export function renderPreviewImageForTitle(container, base64) {
    const li = document.createElement('li')
    const div = document.createElement('div')
    const img = document.createElement('img');
    img.src = `data:image/jpg;base64,${base64}` || '';
    img.alt = 'Image';
    div.appendChild(img)
    li.appendChild(div)
    container.appendChild(li);
}

export function renderAllImages(container, postImages) {
    const ol = document.createElement('ol')

    postImages.forEach(image => {
        const li = document.createElement('li')
        const img = document.createElement('img');
        img.src = `data:image/jpg;base64,${image.base64} ` || '';
        img.alt = image.alt || 'Image';
        li.appendChild(img)
        ol.appendChild(li)
        container.appendChild(ol);
    });
}

export function renderPostComments(container, postComment) {
    const commentsUl = document.createElement('ul');
    postComment.forEach(comment => {
        const commentLi = document.createElement('li');
        commentLi.textContent = JSON.stringify(comment);
        commentsUl.appendChild(commentLi);
    });
    const commentsTitle = document.createElement('li');
    commentsTitle.textContent = 'Comments:';
    commentsTitle.appendChild(commentsUl);
    container.appendChild(commentsTitle);
}

