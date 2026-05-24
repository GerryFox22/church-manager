import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { API_ENDPOINTS } from '../constants/api.constants';
import { ChurchEvent, EventFormValue } from '../../shared/models/event.model';

@Injectable({ providedIn: 'root' })
export class EventsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}${API_ENDPOINTS.events}`;

  getAll(): Observable<ChurchEvent[]> {
    return this.http.get<ChurchEvent[]>(this.baseUrl);
  }

  create(value: EventFormValue): Observable<ChurchEvent> {
    return this.http.post<ChurchEvent>(this.baseUrl, this.toFormData(value));
  }

  update(id: number, value: EventFormValue): Observable<ChurchEvent> {
    return this.http.put<ChurchEvent>(`${this.baseUrl}/${id}`, this.toFormData(value));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  private toFormData(value: EventFormValue): FormData {
    const formData = new FormData();
    formData.append('title', value.title);
    formData.append('description', value.description);
    formData.append('eventDate', value.eventDate);
    formData.append('location', value.location);

    if (value.image) {
      formData.append('image', value.image);
    }

    return formData;
  }
}
