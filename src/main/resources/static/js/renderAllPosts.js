await renderAllPosts()

async function renderAllPosts() {
    const posts = await getAllPosts();
    const postContainer = document.querySelector('#post-container');

    console.log(posts)

    posts.forEach(post => {
        const ul = document.createElement('ul');
        const li = document.createElement('li')

        renderPost(post, ul, li)

        postContainer.appendChild(ul);
    });
}

export default function renderPost(post, container, htmlElement) {
    Object.entries(post).forEach(([key, value]) => {
        if (key === 'commentList' && Array.isArray(value)) {
            renderPostComments(container, value);

        } else if (key === 'imageList' && Array.isArray(value)) {
            renderPreviewImage(container, value)

        } else {
            renderDefaultPostProps(container, key, value, htmlElement)
        }
    });
    renderButtonForDeletePost(container, post.id)
    renderAboutPostLink(container, post.article)
}

function renderAboutPostLink(container, postArticle) {
    const a = document.createElement('a');
    a.href = `/aboutPost/${postArticle}`;
    a.textContent = 'Посмореть подробней';
    container.appendChild(a);
}

function renderDefaultPostProps(container, postKey, postValue, htmlElement) {
    if (postKey !== 'id' && postKey !== "article") {
        if (postKey === 'dateOfCreate') {
            const dateOfCreate = postValue.split('-');
            dateOfCreate[0] = dateOfCreate[0].slice(2);

            const formattedDateOfCreate = dateOfCreate.join(' ')
            console.log(formattedDateOfCreate);

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

function renderButtonForDeletePost(htmlElement, id) {
    const button = document.createElement('button');
    button.type = 'submit';
    button.textContent = 'Удалить пост';
    button.onclick = () => deleteThisPost(id);
    htmlElement.appendChild(button);
}

async function deleteThisPost(id) {
    await fetch(`posts/${id}`, {method: 'DELETE'});
    window.location.reload();
}

function renderPreviewImage(container, postImages) {
    const li = document.createElement('li')
    const previewImage = postImages[0];

    const div = document.createElement('div')
    const img = document.createElement('img');
    img.src = `data:image/jpg;base64,${previewImage.base64} ` || '';
    img.alt = previewImage.alt || 'Image';
    div.appendChild(img)
    li.appendChild(div)
    container.appendChild(li);
}

function renderAllImages(container, postImages) {
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

function renderPostComments(container, postComment) {
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

async function getAllPosts() {
    const response = await fetch('posts/all');
    return await response.json()
}

// TODO -> Посмотреть на такую логику:
// TODO -> Рендерить только первое изображение в большом формате, а все остальные изображения(если присутствуют) добавлять как маленькие изображения