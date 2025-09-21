import { Router } from 'express'
import { createMeme, getMemes } from '../controllers/memeController.js'

const router = Router()
router.post('/', createMeme)
router.get('/', getMemes)
export default router
