
import http from '../http-common';
import INewsData from "../types/news.type";

class NewsDataService {
  getAll() {
    return http.get<Array<INewsData>>("/api/news");
  }

  get(id: string) {
    return http.get<INewsData>(`/api/news/${id}`);
  }

  create(data: INewsData) {
    return http.post<INewsData>("/api/news", data);
  }

  update(data: INewsData, id: any) {
    return http.put<any>(`/api/news/${id}`, data);
  }

  delete(id: any) {
    return http.delete<any>(`/api/news/${id}`);
  }

  deleteAll() {
    return http.delete<any>(`/api/news`);
  }

  findByTitle(title: string) {
    return http.get<Array<INewsData>>(`/api/news/search/${title}`);
  }
}

export default new NewsDataService();