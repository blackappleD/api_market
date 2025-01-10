import dayjs from 'dayjs'

export const formatDate = (date: string | Date, format = 'YYYY-MM-DD HH:mm:ss') => {
  return dayjs(date).format(format)
}

export const formatDateRange = (dates: [Date, Date]) => {
  return dates.map(date => formatDate(date, 'YYYY-MM-DD'))
}