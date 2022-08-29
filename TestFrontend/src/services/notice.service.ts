
import http from '../http-common';
import INoticeData from "../types/notice.type";

class NoticeDataService {
  getAll() {
    return http.get<Array<INoticeData>>("/api/notices");
  }

  get(id: string) {
    return http.get<INoticeData>(`/api/notices/${id}`);
  }

  create(data: INoticeData) {
    return http.post<INoticeData>("/api/notices", data);
  }

  update(data: INoticeData, id: any) {
    return http.put<any>(`/api/notices/${id}`, data);
  }

  delete(id: any) {
    return http.delete<any>(`/api/notices/${id}`);
  }

  deleteAll() {
    return http.delete<any>(`/api/notices`);
  }

  findByTitle(title: string) {
    return http.get<Array<INoticeData>>(`/notices?title=${title}`);
  }
}

export default new NoticeDataService();