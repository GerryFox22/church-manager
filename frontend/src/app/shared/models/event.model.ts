export interface ChurchEvent {
  id: number;
  title: string;
  description: string;
  eventDate: string;
  location: string;
  imageUrl: string | null;
}

export interface EventFormValue {
  title: string;
  description: string;
  eventDate: string;
  location: string;
  image?: File | null;
}
