import express from 'express'
import dotenv from 'dotenv'
import helmet from 'helmet'
import cors from 'cors'
import morgan from 'morgan'
import { connectDB } from './config/db.js'
import memeRoutes from './routes/memes.js'

dotenv.config()

// debug (check that .env is loading)
console.log('Loaded MONGODB_URI:', process.env.MONGODB_URI)

const app = express()

app.use(helmet())
app.use(cors())
app.use(express.json({ limit: '2mb' }))
app.use(morgan('dev'))

app.get('/health', (req, res) => {
  res.json({ ok: true, uptime: process.uptime() })
})

app.use('/memes', memeRoutes)

app.use((req, res, next) => res.status(404).json({ error: 'Route not found' }))
app.use((err, req, res, next) => {
  console.error(err)
  res.status(err.status || 500).json({ error: err.message || 'Server error' })
})

const PORT = process.env.PORT || 4000
connectDB()
  .then(() => app.listen(PORT, () => console.log(`MemeStream API running on ${PORT}`)))
  .catch(err => {
    console.error('Failed to connect DB', err)
    process.exit(1)
  })
