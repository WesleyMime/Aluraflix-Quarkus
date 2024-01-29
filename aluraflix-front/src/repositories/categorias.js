import config from '../config';

const URL_CATEGORIES = `${config.URL_BACKEND_TOP}/categorias`;

function create(objetoDaCategoria, token) {
  return fetch(`${URL_CATEGORIES}`, {
    method: 'POST',
    headers: {
      'Content-type': 'application/json',
      'Authorization': `Bearer ${token}`,
    },
    body: JSON.stringify(objetoDaCategoria),
  })
    .then(async (respostaDoServidor) => {
      if (respostaDoServidor.ok) {
        const resposta = await respostaDoServidor.json();
        return resposta;
      }

      throw new Error('Não foi possível cadastrar os dados :(');
    });
}

function getAll(token) {
  return fetch(`${URL_CATEGORIES}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
    }})
    .then(async (respostaDoServidor) => {
      if (respostaDoServidor.ok) {
        const resposta = await respostaDoServidor.json();
        return resposta.items;
      }
      
      throw new Error('Não foi possível pegar os dados :(');
    });
  }
  
  function getAllWithVideos(token) {
    return fetch(`${URL_CATEGORIES}?_embed=videos`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      }})
    .then(async (respostaDoServidor) => {
      if (respostaDoServidor.ok) {
        const resposta = await respostaDoServidor.json();
        return resposta.items;
      }

      throw new Error('Não foi possível pegar os dados :(');
    });
}

export default {
  getAllWithVideos,
  getAll,
  create
};
