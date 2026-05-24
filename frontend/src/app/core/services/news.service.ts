import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { API_ENDPOINTS } from '../constants/api.constants';
import { News, NewsFormValue } from '../../shared/models/news.model';

@Injectable({ providedIn: 'root' })
export class NewsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}${API_ENDPOINTS.news}`;

  getAll(): Observable<News[]> {
    return this.http.get<News[]>(this.baseUrl);
  }

  create(value: NewsFormValue): Observable<News> {
    return this.http.post<News>(this.baseUrl, this.toFormData(value));
  }

  update(id: number, value: NewsFormValue): Observable<News> {
    return this.http.put<News>(`${this.baseUrl}/${id}`, this.toFormData(value));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  private toFormData(value: NewsFormValue): FormData {
    const formData = new FormData();
    formData.append('title', value.title);
    formData.append('content', value.content);

    if (value.image) {
      formData.append('image', value.image);
    }

    return formData;
  }
}
