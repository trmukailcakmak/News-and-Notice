
import http from '../http-common';
import ITutorialData from "../types/tutorial.type";

class TutorialDataService {
  getAll() {
    return http.get<Array<ITutorialData>>("/api/tutorials");
  }

  get(id: string) {
    return http.get<ITutorialData>(`/api/tutorials/${id}`);
  }

  create(data: ITutorialData) {
    return http.post<ITutorialData>("/api/tutorials", data);
  }

  update(data: ITutorialData, id: any) {
    return http.put<any>(`/api/tutorials/${id}`, data);
  }

  delete(id: any) {
    return http.delete<any>(`/api/tutorials/${id}`);
  }

  deleteAll() {
    return http.delete<any>(`/api/tutorials`);
  }

  findByTitle(title: string) {
    return http.get<Array<ITutorialData>>(`/tutorials?title=${title}`);
  }
}

export default new TutorialDataService();